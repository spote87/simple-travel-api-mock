app.service('StatisticsDataService', StatisticsDataService);

StatisticsDataService.$inject = [
    '$resource',
    'ConfigurationService'];

function StatisticsDataService($resource, configurationService) {
    var service = this;

    service.getStatisticsResource = getStatisticsResource;
    service.getPropertyLabel = getPropertyLabel;

    var metricsLabelMap = {
        "total.client.error": configurationService.LABEL_CLIENT_ERROR,
        "requests.min.response.time": configurationService.LABEL_MIN_RESPONSE_TIME,
        "total.requests.ok": configurationService.LABEL_RESPONSE_OK,
        "total.server.error": configurationService.LABEL_SERVER_ERROR,
        "total.requests.processed": configurationService.LABEL_TOTAL_REQUESTS_PROCESSED,
        "requests.average.response.time": configurationService.LABEL_AVERAGE_RESPONSE_TIME,
        "requests.max.response.time": configurationService.LABEL_MAX_RESPONSE_TIME
    };

    function getStatisticsResource() {
        var StatisticsDataResource = $resource(configurationService.URL_GET_STATISTICS, {}, {
            query: {
                method: 'GET'
            }
        });
        return StatisticsDataResource;

    }

    function getPropertyLabel(prop) {
        if(prop) {
            return metricsLabelMap[prop];
        } else {
            return "";
        }
    }

}
