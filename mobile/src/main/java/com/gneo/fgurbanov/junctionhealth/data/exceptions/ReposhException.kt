package com.gneo.fgurbanov.junctionhealth.data.exceptions

import java.io.IOException

sealed class ReposhException : IOException() {

    class ValidationFieldsException private constructor() : ReposhException() {
        var apiFields = HashMap<String, String>()
            private set
        var noValidFieldsMap = HashMap<String, String>()
            private set

        companion object {

            fun createApiFieldsException(apiFields: HashMap<String, String>): ValidationFieldsException {
                val apiException = ValidationFieldsException()
                apiException.apiFields = apiFields
                return apiException
            }

            fun createNoValidFieldsException(noValidFields: HashMap<String, String>): ValidationFieldsException {
                val noValidException = ValidationFieldsException()
                noValidException.noValidFieldsMap = noValidFields
                return noValidException
            }
        }
    }

    abstract class ApiException : ReposhException()
}

class AuthorizationException : ReposhException.ApiException()

class DuplicateException : ReposhException.ApiException()

class NetworkConnectionException : ReposhException.ApiException()

class SocketConnectionException(val socketMessage: String) : ReposhException.ApiException()

class SocketSendConnectionException : ReposhException.ApiException()