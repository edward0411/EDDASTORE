package com.example.eddastore.ui.util

import android.content.Context
import android.net.Uri
import com.example.eddastore.R
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/** Copia la imagen al almacenamiento interno y retorna una URI file:// */
fun copyImageToAppStorage(ctx: Context, uri: Uri): String {
    val resolver = ctx.contentResolver
    val ext = when (resolver.getType(uri)?.lowercase()) {
        "image/png" -> "png"
        "image/webp" -> "webp"
        "image/jpg", "image/jpeg" -> "jpg"
        else -> "jpg"
    }
    val dir = File(ctx.filesDir, "images").apply { if (!exists()) mkdirs() }
    val out = File(dir, "img_${UUID.randomUUID()}.$ext")

    resolver.openInputStream(uri).use { input ->
        FileOutputStream(out).use { output ->
            requireNotNull(input) { "No se pudo abrir el InputStream de la URI" }
            input.copyTo(output)
        }
    }
    // Guardamos como file:// para que Glide la pueda leer directo
    return "file://${out.absolutePath}"
}

/** Devuelve el id del drawable por nombre, o null si no existe. */
fun drawableIdFromName(ctx: Context, name: String?): Int? {
    if (name.isNullOrBlank()) return null
    val id = ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
    return if (id != 0) id else null
}

/** Fuente unificada para Glide: content://, file://, ruta absoluta o nombre de drawable. */
fun sourceFromImageName(ctx: Context, ref: String?): Any {
    if (ref.isNullOrBlank()) return R.drawable.placeholder

    val lower = ref.lowercase()
    if (lower.startsWith("content://") || lower.startsWith("file://")) {
        return Uri.parse(ref)
    }

    val f = File(ref)
    if (f.isAbsolute && f.exists()) return f

    return drawableIdFromName(ctx, ref) ?: R.drawable.placeholder
}