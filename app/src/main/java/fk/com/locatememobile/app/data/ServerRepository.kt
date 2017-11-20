package fk.com.locatememobile.app.data

import fk.com.locatememobile.app.data.rest.endpoints.LocationEndpoint
import fk.com.locatememobile.app.data.rest.endpoints.UserEndpoint

/**
 * Created by korpa on 21.10.2017.
 */
class ServerRepository(val userEndpoint: UserEndpoint, val locationEndpoint: LocationEndpoint)
