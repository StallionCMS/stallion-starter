<style lang="scss">
 .todo-app-vue {
     border: 1px solid #CCC;
     max-width: 1000px;
     width: 80%;
     margin-top: 20px;
     margin-left: auto;
     margin-right: auto;
     padding: 20px;
     background-color: #EEE;
     color: #666666;


     h1.todo-header {
         margin: 0px;
         padding: 0px;

     }

     .todo-footer {
         margin-top: 2em;
         border-top: 1px solid #bbb;
         color: #555;
         padding-top: 1em;
         text-align: center;
     }
     .done-todo .todo-name {
         text-decoration: line-through;
     }
     .todo-div {

         [type="checkbox"] {
             vertical-align: middle;
         }
         margin-bottom: .25em;
         .btn-edit {
             visibility: hidden;
         }
         .todo-name, todo-date, .btn-edit {
             vertical-align: middle;
         }
     }
     .todo-div:hover {
         .btn-edit {
             visibility: visible;
         }
     }
 }

</style>

<template>
    <div class="todo-app-vue">
        <h1 class="todo-header">To-dos</h1>
        <div v-if="!$store.state.user.authorized">
            <div class="p">You are not logged in.</div>
            <div class="p">
                <a href="/st-users/login">Log in</a> |
                <a href="/st-users/register">Register</a>
            </div>
        </div>
        <div v-else >
            <div class="row">
                <div class="col-md-6">
                    <h5 style="margin-bottom: .5em;">Your To-dos</h5>
                    <div v-for="todo in todos" :class="['todo-div', todo.done ? 'done-todo' : 'active-todo']">
                        <div>
                            <input type="checkbox" @click="toggleDone(todo)" v-model="todo.done">
                            <span class="todo-name" style="display: inline-block; width: 270px;">{{ todo.name }}</span>
                            <span class="todo-date" style="display: inline-block; width: 120px; color: #888;">{{ formatDueAt(todo.dueAt) }}</span>
                            <a href="javascript:;" class="btn btn-xs btn-default btn-edit" @click="editTodo(todo)">Edit</a>
                        </div>

                    </div>
                </div>
                <div class="col-md-6">
                    <form @submit.prevent="saveTodo" id="todo-form" >
                        <div class="form-group">
                            <label>Todo name:</label>
                            <div>
                                <input type="text" class="form-control" v-model="activeTodo.name" required="true">
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Due at:</label>
                            <div>
                                <datetime-picker v-model="activeTodo.dueAt"></datetime-picker>
                            </div>
                        </div>
                        <div>
                            <button type="submit" class="btn btn-default st-button-submit">{{ activeTodo.id ? 'Update To-do' : 'Create To-do' }}</button>
                            <a v-if="activeTodo.id" style="float:right;" href="javascript:;" @click="deleteTodo(activeTodo)">Delete</a>
                        </div>
                    </form>
                </div>
            </div><!-- /row -->
            <div class="todo-footer" style="">
                Logged on as {{ $store.state.user.displayName || $store.state.user.username }} | <a href="/st-users/logoff">Log off</a>
            </div>
        </div>
    </div>
</template>


<script>

 module.exports = {
     data: function() {
         return {
             activeTodo: {dueAt: (new Date().getTime()/1000) + 86400},
             isLoading: true,
             todos: []
         };
     },
     created: function() {
         this.onRoute();
     },
     watch: {
         '$route': 'onRoute'
     },
     methods: {
         formatDueAt: function(dueAt) {
             if (!dueAt) {
                 return '';
             }
             return moment.tz(dueAt * 1000, "America/New_York").format('MMMM Do, h:mm a');
         },
         editTodo: function(todo) {
             this.activeTodo = todo;
         },
         onRoute: function() {
             var self = this;
             if (!self.$store.state.user.authorized) {
                 return;
             }
             stallion.request({
                 url: '/api/v1/todos/find-my-todos',
                 method: 'GET',
                 success: function(o) {
                     self.todos = o.pager.items;
                 }
             });
         },
         toggleDone: function(todo) {
             var self = this;
             console.log('todo.done' , todo.done);
             var newValue = todo.done;
             stallion.request({
                 url: '/api/v1/todos/update/' + todo.id,
                 method: 'POST',
                 data: {done: newValue},
                 success: function(o) {

                 }
             });
         },
         saveTodo: function() {
             var self = this;
             if (this.activeTodo.id) {
                 this.updateTodo(self.activeTodo);
             } else {
                 this.createTodo(self.activeTodo);
             }

         },
         deleteTodo: function(todo) {
             var self = this;
             stallion.request({
                 url: '/api/v1/todos/delete/' + self.activeTodo.id,
                 method: 'POST',
                 success: function(o) {
                     self.activeTodo = {dueAt: (new Date().getTime()/1000) + 86400};
                     self.onRoute();
                 }
             });
         },
         createTodo: function(todo) {
             var self = this;
             stallion.request({
                 url: '/api/v1/todos/create',
                 method: 'POST',
                 form: $(self.$el).find('#todo-form'),
                 data: {
                     dueAt: todo.dueAt,
                     name: todo.name
                 },
                 success: function(o) {
                     stallion.showSuccess('To-do created!');
                     self.activeTodo = {dueAt: (new Date().getTime()/1000) + 86400};
                     self.onRoute();
                 }
             });
         },
         updateTodo: function(todo) {
             var self = this;
             stallion.request({
                 url: '/api/v1/todos/update/' + todo.id,
                 method: 'POST',
                 form: $(self.$el).find('#todo-form'),
                 data: {
                     dueAt: todo.dueAt,
                     name: todo.name
                 },
                 success: function(o) {
                     self.activeTodo = {dueAt: (new Date().getTime()/1000) + 86400};
                     self.onRoute();
                 }
             });
         }

     }
}

</script>
