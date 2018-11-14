app.controller('AirportListController', AirportListController);

AirportListController.$inject = ['AirportsService', 'NgTableParams'];

function AirportListController(airportsService, NgTableParams) {
    var vm = this;

    vm.airports = airportsService.getAirports();

    vm.airportsTable = new NgTableParams({count: 10}, {dataset: vm.airports});

}

