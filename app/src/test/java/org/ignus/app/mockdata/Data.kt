package org.ignus.app.mockdata

import org.ignus.app.db.models.Event
import org.ignus.app.db.models.EventCategory
import org.ignus.app.db.models.Location
import org.ignus.app.db.models.Organiser

const val VALID_JWT_TOKEN = "Some Valid JWT token"

val eventCategoryList: List<EventCategory> = listOf(
        EventCategory(
                "Event Name",
                "3",
                "https://dummy.com/dummy.jpg",
                "About Text",
                listOf(
                        Event("Event 1",
                                "ECRA009",
                                null,
                                3,
                                20,
                                "2019-02-22T12:00:00+05:30",
                                Location("Location 1", 32.1649665, 74.464656),
                                listOf(
                                        Organiser("Organiser 1", "9999999998", "test1@dummy.com", "https://dummy.com/url1.jpg"),
                                        Organiser("Organiser 2", "9999999999", "test2@dummy.com", "https://dummy.com/url2.jpg")
                                )
                        ),
                        Event("Event 2",
                                "ECRA010",
                                null,
                                5,
                                15,
                                "2019-02-24T12:00:00+05:30",
                                Location("Location 2", 32.4534534, 74.6435643),
                                listOf(
                                        Organiser("Organiser 3", "9999999988", "test3@dummy.com", "https://dummy.com/url3.jpg"),
                                        Organiser("Organiser 4", "9999999911", "test4@dummy.com", "https://dummy.com/url4.jpg")
                                )
                        )
                )
        )
)