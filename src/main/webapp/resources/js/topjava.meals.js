const mealAjaxUrl = "profile/meals/";
const mealFilterAjaxUrl = "filter";
let filterForm;

const ctx = {
    ajaxUrl: mealAjaxUrl,
    ajaxFilterUrl: mealAjaxUrl + mealFilterAjaxUrl
};

ctx.updateTable = function () {
    filterForm = $('#filter');
    $.ajax({
        type: "GET",
        url: ctx.ajaxFilterUrl,
        data: filterForm.serialize()
    }).done(function (data) {
        ctx.datatableApi.clear().rows.add(data).draw();
        successNoty("Filtered");
    });
}

function clearFilter(){
    $("#filter").trigger('reset');
    updateTable();
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});