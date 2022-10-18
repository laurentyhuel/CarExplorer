package com.lyh.carexplorer.domain.repository

import com.lyh.carexplorer.domain.model.UserModel

interface IUserRepository {

    suspend fun getUser(): UserModel?

    suspend fun updateUser(userModel: UserModel)
}