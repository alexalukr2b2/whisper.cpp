package com.whispercppdemo.ui.main

import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.edits.EditsRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ChatGptService(private val apiKey: String) {
    private val openAi = OpenAI(apiKey)

    suspend fun processText(text: String): String {
        val slices = splitTextIntoSlices(text)
        val responses = mutableListOf<String>()

        for (slice in slices) {
            val response = sendChatGptRequestToMakeSummary(slice)
            responses.add(response)
        }

        return responses.joinToString("")
    }

    private fun splitTextIntoSlices(text: String): List<String> {
        val sliceSize = 2000 // Adjust the slice size as needed
        val slices = mutableListOf<String>()

        var index = 0
        while (index < text.length) {
            val endIndex = kotlin.math.min(index + sliceSize, text.length)
            val slice = text.substring(index, endIndex)
            slices.add(slice)
            index += sliceSize
        }

        return slices
    }

    private suspend fun sendChatGptRequestToMakeSummary(text: String): String {
        return withContext(Dispatchers.IO) {


            val summaryResponse = openAi.completion(request = CompletionRequest(
                model = ModelId("text-davinci-003"),
                prompt = "Summarize this text: $text"
            ))

            /* Edit uplne nefungoval
            val summaryResponse = openAi.edit(
                request = EditsRequest(
                    model = ModelId("text-davinci-edit-001"),
                    input = text,
                    instruction = "Create summary of given text"
                )
            )*/
            return@withContext summaryResponse.choices.firstOrNull()?.text ?: ""
        }
    }
}