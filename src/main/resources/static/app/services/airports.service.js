app.service('AirportsService', AirportsService);

AirportsService.$inject = [
    '$resource',
    'ConfigurationService'];

function AirportsService($resource, configurationService) {
    var service = this;

    var airports;

    service.getAirports = getAirports;

    //retrieve airport list from backend service
    service.updateAirportsData = updateAirportsData;

    function updateAirportsData() {
        if (angular.isUndefined(airports) || angular.isArray(airports) && !airports.length) {
            airports = [];
        }
        var AirportsResource = $resource(configurationService.URL_get_airports, {}, {
            query: {
                method: 'GET'
            }
        });

        AirportsResource.query().$promise.then(function (response) {
            if(response && response._embedded && response._embedded.locations) {
                airports = response._embedded.locations;
            }

        }).catch(function (error) {
            console.log('error occured while retrieving airports', error);
        });

    }

    function getAirports() {
        return airports;
    }
}
