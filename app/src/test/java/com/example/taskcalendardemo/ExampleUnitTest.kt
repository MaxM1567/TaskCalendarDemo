package com.example.taskcalendardemo

import com.example.taskcalendardemo.fragments.HomeFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.reflect.Method

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testConvertToDateString() {
        val homeFragment = HomeFragment()

        val timestamp: Long = 1631134800000 // это 2021-09-09 12:00:00

        val method: Method =
            homeFragment::class.java.getDeclaredMethod("convertToDateString", Long::class.java)
        method.isAccessible = true

        val expectedDate = "2021-09-09"
        val result = method.invoke(homeFragment, timestamp) as String

        assertEquals(expectedDate, result)
    }
}