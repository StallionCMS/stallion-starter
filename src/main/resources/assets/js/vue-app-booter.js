

(function() {

    var app = {};
    window.TodoApp = app;

    app.init = function() {

        var store = new Vuex.Store({
            state: {
                user: null
            },
            mutations: {
                user: function(state, user) {
                    state.user = user;
                }
            }
        });
        app.store = store;
        store.commit('user', theApplicationContext.user);

        var vueApp = new Vue({
            store: store
        });
        app.VueApp = vueApp;

        vueApp.$mount('#vue-app-target');

    }


    $(document).ready(app.init);


}());
