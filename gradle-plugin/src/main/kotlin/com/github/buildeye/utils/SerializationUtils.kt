package com.github.buildeye.utils

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.file.Files
import java.nio.file.NotDirectoryException
import java.nio.file.Path

fun <T> serialize(path: Path, obj: T?) {
    ensureWritable(path)

    FileOutputStream(path.toFile()).use { fileOutputStream ->
        ObjectOutputStream(fileOutputStream).use { objectOutputStream ->
            objectOutputStream.writeObject(obj)
        }
    }
}

fun <T> deserialize(path: Path): T {
    FileInputStream(path.toFile()).use { fileInputStream ->
        ObjectInputStream(fileInputStream).use { objectInputStream ->
            @Suppress("UNCHECKED_CAST")
            return objectInputStream.readObject() as T
        }
    }
}

private fun ensureWritable(path: Path) {
    with(path.normalize().toAbsolutePath()) {
        val parent = this.parent

        if (!Files.exists(parent)) {
            Files.createDirectories(parent)
        } else if (!Files.isDirectory(parent)) {
            throw NotDirectoryException(parent.toString())
        }

        if (!Files.exists(this)) {
            Files.createFile(this)
        } else if (!Files.isRegularFile(this) || !Files.isWritable(this)) {
            throw FileAlreadyExistsException(this.toFile())
        }

        return
    }
}