package cz.beesli.cleverlancetest.ui.login

/**
 * User details post authentication that is exposed to the UI
 */
data class LoggedInUserView(
    val bitmapByteArray: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoggedInUserView

        if (bitmapByteArray != null) {
            if (other.bitmapByteArray == null) return false
            if (!bitmapByteArray.contentEquals(other.bitmapByteArray)) return false
        } else if (other.bitmapByteArray != null) return false

        return true
    }

    override fun hashCode(): Int {
        return bitmapByteArray?.contentHashCode() ?: 0
    }
}
