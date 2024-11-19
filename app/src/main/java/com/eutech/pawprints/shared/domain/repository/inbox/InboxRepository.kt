package com.eutech.pawprints.shared.domain.repository.inbox

import com.eutech.pawprints.appointments.data.appointment.Inbox
import com.eutech.pawprints.shared.presentation.utils.Results


interface InboxRepository  {
    suspend fun getAllInboxByUserID(userID : String,result : (Results<List<Inbox>>) -> Unit)
}