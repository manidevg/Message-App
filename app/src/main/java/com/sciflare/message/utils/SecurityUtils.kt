package com.sciflare.message.utils

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object SecurityUtils {

    fun encrypt(rawText: String): String {
        val secretKey = generateKey()
        val secretKeySpec = SecretKeySpec(secretKey, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(secretKey.generateIv()))
        val resultData = cipher.doFinal(rawText.toByteArray(Charsets.UTF_8))
        return String(resultData.encodeBase64())
    }

    fun decrypt(decryptText: String): String {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            val secretKey = generateKey()
            val secretKeySpec = SecretKeySpec(secretKey, "AES")
            cipher.init(
                Cipher.DECRYPT_MODE,
                secretKeySpec,
                IvParameterSpec(secretKey.generateIv())
            )
            return String(cipher.doFinal(decryptText.toByteArray().decodeBase64()))
        } catch (e: Exception) {
            return decryptText
        }
    }

    private fun ByteArray.generateIv(): ByteArray {
        val secretKeyBase64 = Base64.encodeToString(this, Base64.DEFAULT)
        return secretKeyBase64.take(16).toByteArray()
    }

    private fun generateKey(): ByteArray {
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        val bytes = AppController().getSecretKeyPswd().toByteArray()
        digest.update(bytes, 0, bytes.size)
        return digest.digest()
    }

    fun ByteArray.decodeBase64(): ByteArray {
        val table = intArrayOf(
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            62,
            -1,
            -1,
            -1,
            63,
            52,
            53,
            54,
            55,
            56,
            57,
            58,
            59,
            60,
            61,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            0,
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10,
            11,
            12,
            13,
            14,
            15,
            16,
            17,
            18,
            19,
            20,
            21,
            22,
            23,
            24,
            25,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            26,
            27,
            28,
            29,
            30,
            31,
            32,
            33,
            34,
            35,
            36,
            37,
            38,
            39,
            40,
            41,
            42,
            43,
            44,
            45,
            46,
            47,
            48,
            49,
            50,
            51,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1
        )

        val output = ByteArrayOutputStream()
        var position = 0
        while (position < this.size) {
            var b: Int
            if (table[this[position].toInt()] != -1) {
                b = table[this[position].toInt()] and 0xFF shl 18
            } else {
                position++
                continue
            }
            var count = 0
            if (position + 1 < this.size && table[this[position + 1].toInt()] != -1) {
                b = b or (table[this[position + 1].toInt()] and 0xFF shl 12)
                count++
            }
            if (position + 2 < this.size && table[this[position + 2].toInt()] != -1) {
                b = b or (table[this[position + 2].toInt()] and 0xFF shl 6)
                count++
            }
            if (position + 3 < this.size && table[this[position + 3].toInt()] != -1) {
                b = b or (table[this[position + 3].toInt()] and 0xFF)
                count++
            }
            while (count > 0) {
                val c = b and 0xFF0000 shr 16
                output.write(c.toChar().toInt())
                b = b shl 8
                count--
            }
            position += 4
        }
        return output.toByteArray()
    }

    fun ByteArray.encodeBase64(): ByteArray {
        val table =
            (CharRange('A', 'Z') + CharRange('a', 'z') + CharRange(
                '0',
                '9'
            ) + '+' + '/').toCharArray()
        val output = ByteArrayOutputStream()
        var padding = 0
        var position = 0
        while (position < this.size) {
            var b = this[position].toInt() and 0xFF shl 16 and 0xFFFFFF
            if (position + 1 < this.size) b =
                b or (this[position + 1].toInt() and 0xFF shl 8) else padding++
            if (position + 2 < this.size) b =
                b or (this[position + 2].toInt() and 0xFF) else padding++
            for (i in 0 until 4 - padding) {
                val c = b and 0xFC0000 shr 18
                output.write(table[c].toInt())
                b = b shl 6
            }
            position += 3
        }
        for (i in 0 until padding) {
            output.write('='.toInt())
        }
        return output.toByteArray()
    }
}