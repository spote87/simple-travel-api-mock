app.controller('LocationSelectorController', LocationSelectorController);

LocationSelectorController.$inject = [
    'AirportsService'];


function LocationSelectorController(AirportsService) {
    var vm = this;

    init();

    function init() {
        vm.airportList = AirportsService.getAirports();
    }

}

