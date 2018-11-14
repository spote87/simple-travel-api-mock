app.controller('AppController', AppController);


AppController.$inject = [
    'AirportsService'];

function AppController(airportsService) {

    var vm = this;


    vm.getFares = getFares;
    vm.getAirports = getAirports;
    vm.metricsData = metricsData;
    vm.showHome = showHome;

    init();

    function init() {
        vm.fare = false;
        vm.airports = false;
        vm.metrics = false;
        vm.showLandingPage = true;
        airportsService.updateAirportsData();

    }

    function showHome() {
        vm.fare = false;
        vm.airports = false;
        vm.metrics = false;
        vm.showLandingPage = true;

    }

    function getFares() {
        if (vm.airports) {
            vm.airports = false;
        }
        if (vm.metrics) {
            vm.metrics = false;
        }
        vm.showLandingPage = false;
        vm.fare = true;
    }

    function getAirports() {
        if (vm.fare) {
            vm.fare = false;
        }
        if (vm.metrics) {
            vm.metrics = false;
        }
        vm.showLandingPage = false;
        vm.airports = true;
    }

    function metricsData() {
        if (vm.fare) {
            vm.fare = false;
        }
        if (vm.airports) {
            vm.airports = false;
        }
        vm.showLandingPage = false;
        vm.metrics = true;
    }
}


