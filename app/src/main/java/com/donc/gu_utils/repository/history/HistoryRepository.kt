package com.donc.gu_utils.repository.history

import com.donc.gu_utils.data.models.MatchRecords
import com.donc.gu_utils.util.Resource

interface HistoryRepository {
/*Unit Tests should not have external dependencies.
In this case I use injections like this ViewModel <- Repository <- ApiService <- RetrofitInstance
Which means that to write a test for a simple validation function from the viewmodel,
I'd need to instantiate a viewmodel with a repository that contains all the rest.
Usually you can use a "mock" for that, but since at the end of the chain there are some
@Provides from the AppModule, a mock class would not work. The solution would be to make
Instrumented Unit tests which seem excessive for simple validations.
As an alternative, the ViewModel will have an Interface on the constructor, if used on the
activities the ViewModel will have an implemented version received from the AppModule and when
used on a test they will use a fake version that doesn't have that chain of dependencies.
 */

    suspend fun getWinningMatches(player_won : String, page: Int) : Resource<MatchRecords>

    suspend fun getLosingMatches(player_lost : String, page: Int) : Resource<MatchRecords>

}

