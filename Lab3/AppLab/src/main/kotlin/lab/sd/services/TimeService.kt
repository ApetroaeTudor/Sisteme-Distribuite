package lab.sd.services

import lab.sd.interfaces.ITime
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TimeService : ITime{

    override fun getCurrentTime() : String {
        val currentDate = LocalDateTime.now()
        val localDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return localDateFormatter.format(currentDate)
    }
}