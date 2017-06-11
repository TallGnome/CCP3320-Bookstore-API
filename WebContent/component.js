const endpointURL = './api/books/';



var app = new Vue({
    el: '#app',
    data: {
        books : [],
        isLoading : false,
        selectedState : false,
        selected : null,
        originalSelected : null,
        editMode : false,
        createMode : false,
        hasValid: true,
        search : "",
        newEntity: {
            id: -1,
            title: "",
            author: "",
            publisher: "",
            ISBN: "",
            inStock: false,
            language: "",
            subject: ""
        }
    },
    methods: {
        isEmpty : function(txt){
            if(txt.length === 0){
                this.hasValid = false;
            }
            else{
                this.hasValid = true;
            }
            return txt.length === 0;
        },
        cancelEdit: function(){
            this.selected = this.cloneObject(this.originalSelected);
        },
        cancelSelectedState: function(){
            this.selectedState = false;
        },
        show_details: function (i){
            if(!this.createMode){
                this.selected = this.cloneObject(i);
                this.originalSelected = this.cloneObject(this.selected);
                this.selectedState = true;
            }
        } ,
        change_edit_state: function(){
            this.editMode = !this.editMode;
            if(!this.editMode)
                this.cancelEdit();
        },  
        toggleCreateMode: function(){
            if(!this.selectedState)
                this.createMode = !this.createMode;
        },
        cleanNewEntity: function(){
            this.newEntity = {
                id: -1,
                title: "",
                author: "",
                publisher: "",
                ISBN: "",
                inStock: false,
                language: "",
                subject: ""
            };
        },
        searchBook: function(){
            if(this.search.length === 0){
                toastr.warning("No search query defined...");
            }            
            else{
                this.isLoading = true;
                $.ajax({
                    url: endpointURL+"?q="+app.search,
                    contentType: 'application/json',
                    dataType: 'json',
                    type: "GET"
                })
                .done(function(data) {
                    app.isLoading = false;
                    app.books = data;
                    console.log(data);
                })
                .fail(function(xhr) {
                    app.isLoading = false;
                    toastr.error('Error','Book could not be found');
                    console.log('Ajax error, status: ' + xhr.status);
                });
                this.$forceUpdate();
            }
        },
        create_book: function () {
            if(!this.hasValid){
                toastr.warning("There are empty fields in the form");
            }
            else{
                this.isLoading = true;
                $.ajax({
                    url: endpointURL,
                    dataType: "json",
                    contentType: "application/json",
                    type: "POST",
                    data: JSON.stringify(app.newEntity)
                })
                .done(function(data) {
                    app.isLoading = false;
                    console.log(data);
                    app.books.push(data);
                    app.cleanNewEntity();
                    toastr.success('Book created');
                    console.log('Ajax success');
                })
                .fail(function(xhr) {
                    app.isLoading = false;
                    toastr.error('Error','Book could not be created');
                    console.log('Ajax error, status: ' + xhr.status);
                });
                this.createMode = false;
                this.$forceUpdate();
            }
        },
        load_books: function () {
            this.isLoading = true;
            $.ajax({
                url: endpointURL,
                contentType: 'application/json',
                dataType: 'json',
                type: "GET"
            })
            .done(function(data) {
                app.isLoading = false;
                app.books = data;
                toastr.success('Books fetched');
                console.log('Ajax success');
            })
            .fail(function(xhr) {
                app.isLoading = false;
                toastr.error('Error','Books could not be fetched');
                console.log('Ajax error, status: ' + xhr.status);
            });
            this.$forceUpdate();
        },
        saveUpdate: function(){
            if(!this.hasValid){
                toastr.warning("There are empty fields in the form");
            }
            else{
                this.isLoading = true;
                $.ajax({
                    url: endpointURL,
                    method: "PUT",
                    contentType: 'application/json',
                    dataType: 'json',
                    data: JSON.stringify(app.selected)
                })
                .done(function(data) {
                    app.isLoading = false;
                    app.editMode = false;
                    console.log(data);
                    toastr.success('Update successfull!');
                    console.log('Ajax success');
                    app.originalSelected = app.selected;
                })
                .fail(function(xhr) {
                    app.isLoading = false;
                    toastr.error('Error','Update failed');
                    console.log('Ajax error, status: ' + xhr.status);
                });
                this.$forceUpdate();
            }
        },
        deleteEntity: function(){
            this.isLoading = true;
            $.ajax({
                url: endpointURL+app.selected.id,
                method: "DELETE",
                contentType: 'application/json',
                dataType: 'json'
            })
            .done(function(data) {
                app.isLoading = false;
                app.editMode = false;
                console.log(data);
                toastr.success('Item successfully deleted');
                app.load_books();
                app.selected = null;
                console.log('Ajax success');
            })
            .fail(function(xhr) {
                app.isLoading = false;
                toastr.error('Error','Delete failed');
                console.log('Ajax error, status: ' + xhr.status);
            });
            this.$forceUpdate();
        },
        cloneObject : function (obj){
            if(obj == null || typeof(obj) != 'object')
                return obj;

            var temp = new obj.constructor(); 
            for(var key in obj)
                temp[key] = this.cloneObject(obj[key]);

            return temp;
        }
    },
    mounted: function() {
        this.load_books();
    }

   
});
 