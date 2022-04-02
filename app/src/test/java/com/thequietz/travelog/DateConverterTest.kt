package com.thequietz.travelog

import com.thequietz.travelog.util.nextDate
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DateConverterTest {
    @Test
    fun nextDate_date_returnIncreasedDateOnlyForTheDay() {
        // Given a string representing the date
        val date = "2022.02.16"

        // When the next date of the date
        val nextDate = date.nextDate()

        // Then increased date only for the day
        assertThat(nextDate, `is`("2022.02.17"))
    }

    @Test
    fun nextDate_date_returnIncreasedDateUntilTheMonth() {
        // Given a string representing the date
        val date = "2022.01.31"

        // When the next date of the date
        val nextDate = date.nextDate()

        // Then increased date until the month
        assertThat(nextDate, `is`("2022.02.01"))
    }

    @Test
    fun nextDate_date_returnIncreasedDateUntilTheYear() {
        // Given a string representing the date
        val date = "2021.12.31"

        // When the next date of the date
        val nextDate = date.nextDate()

        // Then increased date until the year
        assertThat(nextDate, `is`("2022.01.01"))
    }

    @Test
    fun nextDate_dateOfLeafYear_returnIncreasedDate() {
        // Given a string representing the date of leaf year
        val date = "2020.02.28"

        // When the next date of the date
        val nextDate = date.nextDate()

        // Then increased date
        assertThat(nextDate, `is`("2020.02.29"))
        assertThat(nextDate, not("2020.03.01"))
    }

    @Test
    fun nextDate_dateOfNotLeafYear_returnIncreasedDate() {
        // Given a string representing the date of not leaf year
        val date = "2021.02.28"

        // When the next date of the date
        val nextDate = date.nextDate()

        // Then increased date
        assertThat(nextDate, `is`("2021.03.01"))
        assertThat(nextDate, not("2021.02.29"))
    }
}
