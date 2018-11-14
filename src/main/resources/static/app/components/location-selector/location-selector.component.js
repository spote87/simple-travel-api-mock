app.component('locationSelector', {
    templateUrl: 'app/components/location-selector/location-selector.template.html',
    controller: 'LocationSelectorController',
    controllerAs: 'locationSelectorController',
    bindings: {
        selectedLocation: '='
    }
});