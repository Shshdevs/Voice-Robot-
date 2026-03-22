package com.hotelka.voicerobot.domain.exceptions

import java.lang.Exception

class AudioRecorderNotInitialized(): Exception("AudioRecord could not be initialized")
class AudioRecorderPermissionNotGranted(): Exception("RECORD_AUDIO permission is not granted" )