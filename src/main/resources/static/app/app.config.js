/*
app.config([
    '$routeProvider',
    function ($routeProvider) {
        $routeProvider
            .when("/", {
                templateUrl: "index.html",
                controller: "travelPriceEngineController"
            })
            .otherwise({
                redirectTo: "/"
            });
    }]);
*/
app.constant('ConfigurationService', getAppConstants());

function getAppConstants() {
    return {
        APP_NAME: 'travelPriceEngineApp',
        URL_get_airports: '/airports',
        URL_get_fares: '/fares',
        URL_GET_STATISTICS: '/appStatistics',
        LOCATION_NOT_SELECTED_ERROR_MESSAGE: 'Origin and/or destination not selected. Please select origin and destination.',
        FARE_DATA_NOT_AVAILABLE_ERROR: 'Fares data not available or selected origin and destination.',
        LABEL_CLIENT_ERROR: "Total Client Errors (4xx)",
        LABEL_SERVER_ERROR: "Total Server Errors (4xx)",
        LABEL_RESPONSE_OK: "Total Responses OK (200)",
        LABEL_AVERAGE_RESPONSE_TIME: "Average Response Time of All Requests",
        LABEL_MIN_RESPONSE_TIME: "Minimum Response Time of All Requests",
        LABEL_MAX_RESPONSE_TIME: "Maximum Response Time of All Requests",
        LABEL_TOTAL_REQUESTS_PROCESSED: "Total Requests Processed",

    };
}
