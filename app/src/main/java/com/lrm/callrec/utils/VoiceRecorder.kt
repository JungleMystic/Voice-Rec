package com.lrm.callrec.utils

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import android.util.Log
import com.lrm.callrec.constants.APP_FOLDER_NAME
import com.lrm.callrec.constants.TAG
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class VoiceRecorder(private val context: Context) {

    private var recorder: MediaRecorder? = null
    var recordingStatus = "Idle"

    fun createDirectory() {
        Log.i(TAG, "createDirectory is called")
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APP_FOLDER_NAME)
        if (!file.exists()){
            file.mkdir()
            Log.i(TAG, "Folder is created")
        } else {
            Log.i(TAG, "Folder is already created")
        }
    }

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    fun startRecording() {
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(getOutputPath())
            prepare()
            start()
            recorder = this
            recordingStatus = "Recording..."
        }
    }

    fun pauseRecording() {
        recorder?.pause()
        recordingStatus = "Paused"
        Log.i(TAG, "pauseRecording is called")
    }

    fun resumeRecording() {
        recorder?.resume()
        recordingStatus = "Recording..."
        Log.i(TAG, "resumeRecording is called")
    }

    fun stopRecording() {
        recorder?.stop()
        recorder?.release()
        recorder = null
        recordingStatus = "Recording completed"
        Log.i(TAG, "stopRecording is called")
    }

    private fun getOutputPath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Voice Rec/${getFileName()}"
    }

    private fun getFileName(): String {
        val sdf = SimpleDateFormat("dd-MM-yyy hh-mm a", Locale.getDefault())
        val date = Date()
        return "VoiceRec${Random.nextInt(999, 9999)} ${sdf.format(date)}.m4a"
    }
}