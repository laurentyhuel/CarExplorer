package com.lyh.carexplorer.domain

import com.lyh.carexplorer.domain.model.UserModel
import com.lyh.carexplorer.domain.repository.IUserRepository

class UserUseCase(private val userRepository: IUserRepository) {

    suspend fun getUser() = userRepository.getUser()

    suspend fun updateUser(userModel: UserModel) = userRepository.updateUser(userModel)
}