package api

import model.Account
import model.User

interface IUserApiService {
    suspend fun createUser(account: Account): User
}