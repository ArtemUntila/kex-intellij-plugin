package org.vorpal.research.kex.plugin.util

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.vorpal.research.kex.plugin.TITLE

private val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup(TITLE)

fun notifyInfo(project: Project, title: String, content: String) {
    notify(project, title, content, NotificationType.INFORMATION)
}

fun notifyWarn(project: Project, title: String, content: String) {
    notify(project, title, content, NotificationType.WARNING)
}

fun notifyError(project: Project, title: String, content: String) {
    notify(project, title, content, NotificationType.ERROR)
}

fun notify(project: Project, title: String, content: String, type: NotificationType) {
    notificationGroup
        .createNotification(title, content, type)
        .notify(project)
}