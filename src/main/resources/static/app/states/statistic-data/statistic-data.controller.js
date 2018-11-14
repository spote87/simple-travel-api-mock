app.controller('StatisticDataController', StatisticDataController);

StatisticDataController.$inject = ['StatisticsDataService'];

function StatisticDataController(statisticsDataService) {
    var vm = this;

    vm.statistics = [];

    getStatisticsData();

    function getStatisticsData() {
        var StatisticsResource = statisticsDataService.getStatisticsResource();

        StatisticsResource.query().$promise.then(function (response) {
            console.log(response);
            if (response) {
                Object.keys(response).forEach(function (key) {
                    var label = statisticsDataService.getPropertyLabel(key);
                    if (label && label.length) {
                        var obj = {key: label, value: response[key]};
                        vm.statistics.push(obj);
                    }
                });
            }

        }).catch(function (error) {
            console.log('error occured while retrieving metrics.', error);
        });
    }
}

