package com.project.ekanfinal.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.ekanfinal.model.data.ProductModel
import com.project.ekanfinal.model.data.UserModel
import com.project.ekanfinal.model.repository.ProductRepository
import com.project.ekanfinal.model.repository.UserRepository
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.project.ekanfinal.model.data.AddressModel
import com.project.ekanfinal.model.data.OrderModel
import com.project.ekanfinal.model.repository.OrderHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class OrderViewModel(

    private val userRepo: UserRepository = UserRepository(),
    private val productRepo: ProductRepository = ProductRepository(),
    private val historyRepo: OrderHistoryRepository = OrderHistoryRepository(),


    ) : ViewModel() {

    val user = mutableStateOf(UserModel())
    val productList = mutableStateListOf<ProductModel>()
    val total = mutableFloatStateOf(0f)
    val ongkir = mutableFloatStateOf(0f)
    val totalFinal = mutableFloatStateOf(0f)

    val diskonMember = mutableFloatStateOf(0f)
    val diskonOngkir = mutableFloatStateOf(0f)
    val totalSetelahDiskon = mutableFloatStateOf(0f)

    private val _orders = MutableLiveData<List<OrderModel>>()
    val orders: LiveData<List<OrderModel>> = _orders

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error




    val currentUid: String?
        get() = FirebaseAuth.getInstance().currentUser?.uid


    fun setUser(uid: String) {
        historyRepo.setUserId(uid)
        loadOrders(uid)
    }

    fun loadOrders(uid: String) {
        historyRepo.getAllOrders(
            onResult = { list ->
                _orders.postValue(list)
            },
            onError = { e ->
                _error.postValue(e.message ?: "Unknown error")
            }
        )
    }

    private val _selectedOrder = MutableLiveData<OrderModel?>()
    val selectedOrder: LiveData<OrderModel?> = _selectedOrder

    fun loadOrderById(orderId: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val userOrderRef = FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("orders")
            .document(orderId)

        userOrderRef.get()
            .addOnSuccessListener { doc ->
                val order = doc.toObject(OrderModel::class.java)
                _selectedOrder.postValue(order)
            }
            .addOnFailureListener {
                _error.postValue("Gagal memuat pesanan: ${it.message}")
            }
    }

    fun updateOrderStatus(
        orderId: String,
        newStatus: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val uid = currentUid
        Log.d("ViewModel", "updateOrderStatus called with orderId=$orderId, uid=$uid")
        if (uid == null) {
            Log.e("ViewModel", "UID is null")
            onFailure(IllegalStateException("UID is null"))
            return
        }

        historyRepo.updateOrderStatus(uid, orderId, newStatus, {
            Log.d("ViewModel", "updateOrderStatus success callback")
            loadOrders(uid)
            onSuccess()
        }, { error ->
            Log.e("ViewModel", "updateOrderStatus failure callback", error)
            onFailure(error)
        })
    }




    init {
        fetchCheckoutData()

    }

    fun ongkirPersen(): Float {
        return 0.1f
    }

    fun diskonMemberPersen(): Float {
        return 0.1f
    }

    fun diskonOngkirPersen(): Float {
        return 0.5f
    }


    private fun calculate() {
        total.value = productList.sumOf {
            val qty = user.value.cartItems[it.id] ?: 0
            ((it.harga.toFloatOrNull() ?: 0f) * qty).toDouble()
        }.toFloat()

        ongkir.value = total.value * ongkirPersen()

        diskonMember.value = total.value * diskonMemberPersen()

        diskonOngkir.value = ongkir.value * diskonOngkirPersen()

        totalFinal.value = total.value + ongkir.value

        totalSetelahDiskon.value = "%.2f".format(
            (totalFinal.value - diskonMember.value) + (ongkir.value - diskonOngkir.value)
        ).toFloat()
    }

    fun fetchCheckoutData() {
        viewModelScope.launch {
            val currentUser = userRepo.getCurrentUser()
            if (currentUser != null) {
                user.value = currentUser
                val products = productRepo.getProductsByIds(currentUser.cartItems.keys.toList())
                productList.clear()
                productList.addAll(products)
                calculate()
            }
        }
    }

    fun placeOrder(context: Context, selectedAddress: AddressModel?) {
        val userData = user.value
        val cartItems = userData.cartItems

        if (cartItems.isEmpty()) {
            Toast.makeText(context, "Keranjang kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedAddress == null) {
            Toast.makeText(context, "Pilih alamat pengiriman terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return
        }


        val orderId = Firebase.firestore.collection("orders").document().id

        val orderModel = OrderModel(
            id = orderId,
            address = selectedAddress,
            status = "belum bayar",
            items = cartItems,
            total = totalSetelahDiskon.value
        )


        val firestore = Firebase.firestore

        val globalOrderRef = firestore.collection("orders").document(orderId)
        val userOrderRef = firestore.collection("users")
            .document(userData.uid)
            .collection("orders")
            .document(orderId)

        firestore.runBatch { batch ->
            batch.set(globalOrderRef, orderModel)
            batch.set(userOrderRef, orderModel)
            batch.update(firestore.collection("users").document(userData.uid), "cartItems", emptyMap<String, Int>())
        }.addOnSuccessListener {
            user.value = user.value.copy(cartItems = emptyMap())
            productList.clear()
            calculate()
            Toast.makeText(context, "Pesanan berhasil dibuat!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Gagal membuat pesanan!", Toast.LENGTH_SHORT).show()
        }
    }

}