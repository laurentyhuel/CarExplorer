package com.lyh.carexplorer.data

import com.lyh.carexplorer.data.local.dao.UserDao
import com.lyh.carexplorer.data.mapper.toEntity
import com.lyh.carexplorer.data.mapper.toModel
import com.lyh.carexplorer.domain.model.UserModel
import com.lyh.carexplorer.domain.repository.IUserRepository

class UserRepository(
    private val userDao: UserDao,
) : IUserRepository {

    override suspend fun getUser(): UserModel? =
        userDao.getUsers().firstOrNull()?.toModel()

    override suspend fun updateUser(userModel: UserModel) {
        userDao.insertOrUpdateUser(userModel.toEntity())
    }
}