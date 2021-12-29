$(document).ready( function () {
    $('#student-table').DataTable();


    $("#delete_student_button").on("click", function(e) {
        e.preventDefault();
        console.log("inside");

        var id = $(this).attr("data-id");
        var href =$("#delete_button_modal").attr("href").replace("_id_", id);
        $("#delete_button_modal").attr("href", href);


        $("#delete_student_modal").modal("show");



    })
} );