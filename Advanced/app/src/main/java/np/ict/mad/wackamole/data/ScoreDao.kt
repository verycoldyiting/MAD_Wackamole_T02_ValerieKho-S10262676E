package np.ict.mad.wackamole.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoreDao {

    @Insert
    suspend fun insertScore(score: ScoreEntity)

    @Query(
        "SELECT MAX(score) FROM scores WHERE userId = :userId"
    )
    suspend fun getPersonalBest(userId: Int): Int?

    @Query(
        """
        SELECT users.username, MAX(scores.score) AS bestScore
        FROM scores
        INNER JOIN users ON scores.userId = users.userId
        GROUP BY users.userId
        ORDER BY bestScore DESC
        """
    )
    suspend fun getLeaderboard(): List<LeaderboardResult>
}
