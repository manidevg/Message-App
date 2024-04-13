package com.sciflare.message.utils

class AppController {

    external fun getSecretKeyPswd():String

    companion object {
        init {
            System.loadLibrary("message")
        }
    }
}