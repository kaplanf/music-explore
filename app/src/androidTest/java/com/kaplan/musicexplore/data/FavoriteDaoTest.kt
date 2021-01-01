package com.kaplan.musicexplore.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kaplan.musicexplore.util.getValue
import com.kaplan.musicexplore.ui.favorite.data.FavoriteDao
import com.kaplan.musicexplore.util.testFavoriteA
import com.kaplan.musicexplore.util.testFavoriteB
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDaoTest : DbTest() {
    private lateinit var favoriteDao: FavoriteDao
    private val setA = testFavoriteA.copy()
    private val setB = testFavoriteB.copy()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before fun createDb() {
        favoriteDao = db.favoriteDao()
        runBlocking {
            favoriteDao.insertAll(listOf(setA, setB))
        }
    }

    @Test fun testGetLiked() {
        val list = getValue(favoriteDao.getLikedTracks())
        assertThat(list.size, equalTo(2))

        assertThat(list[0], equalTo(setA))
        assertThat(list[1], equalTo(setB))
    }
}