package com.example.putdelapi

class UsersData : ArrayList<UsersData.UsersDataItem>(){
    data class UsersDataItem(
        val location: String,
        val name: String
    )
}