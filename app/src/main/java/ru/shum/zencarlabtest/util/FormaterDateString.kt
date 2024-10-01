package ru.shum.zencarlabtest.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.Calendar
import kotlin.math.absoluteValue

class DateTransformation(private val mask: String = "##/##/####") : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        val out = StringBuilder()
        var maskIndex = 0

        text.forEach { char ->
            while (maskIndex < mask.length && specialSymbolsIndices.contains(maskIndex)) {
                out.append(mask[maskIndex])
                maskIndex++
            }
            out.append(char)
            maskIndex++
        }

        return TransformedText(AnnotatedString(out.toString()), offsetTranslator())
    }

    private fun offsetTranslator(): OffsetMapping = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val absoluteOffset = offset.absoluteValue
            if (absoluteOffset == 0) return 0

            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < absoluteOffset
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}
