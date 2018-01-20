package fk.com.locatememobile.app.data.rest

import fk.com.locatememobile.app.data.rest.endpoints.LocationEndpoint
import fk.com.locatememobile.app.data.rest.endpoints.UserEndpoint
import fk.com.locatememobile.app.data.rest.endpoints.UserFriendsEndpoint

class ServerRepository(val userEndpoint: UserEndpoint,
                       val locationEndpoint: LocationEndpoint,
                       val userFriendsEndpoint: UserFriendsEndpoint)
