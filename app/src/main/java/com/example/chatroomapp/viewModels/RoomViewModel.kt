package com.example.chatroomapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatroomapp.Injection
import com.example.chatroomapp.data.Room
import com.example.chatroomapp.data.RoomRepository
import com.example.chatroomapp.data.UserRepository
import com.example.chatroomapp.data.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {

    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms

    private val _userData = MutableStateFlow<Pair<String, String>?>(null)
    val userData: StateFlow<Pair<String, String>?> = _userData

    private val roomRepository: RoomRepository
    private val userRepository: UserRepository
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        val firestore = Injection.instance()
        roomRepository = RoomRepository(firestore)
        userRepository = UserRepository(auth, firestore)
        loadRooms()
        fetchUserData()
    }

    fun createRoom(name: String) {
        viewModelScope.launch {
            roomRepository.createRoom(name)
        }
    }

    fun loadRooms() {
        viewModelScope.launch {
            when (val result = roomRepository.getRooms()) {
                is Result.Success -> _rooms.value = result.data
                is Result.Error -> {
                    // Handle error, maybe set an error state
                }
            }
        }
    }

    private fun fetchUserData() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> {
                    val user = result.data
                    _userData.value = Pair("${user.firstName} ${user.lastName}", user.email)
                }
                is Result.Error -> {
                    // Handle error, maybe set an error state
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _userData.value = null
    }
}