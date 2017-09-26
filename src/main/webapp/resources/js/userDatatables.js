var ajaxUrl = "ajax/admin/users/";
var datatableApi;

function updateTable() {
    $.get(ajaxUrl, updateTableByData);
}

function enable(chkbox, id) {
    var enabled = chkbox.is(":checked");
    $.ajax({
        url: ajaxUrl + id,
        type: "POST",
        data: "enabled=" + enabled,
        success: function () {
            chkbox.closest("tr").toggleClass("disabled");
            successNoty(enabled ? "Enabled" : "Disabled");
        },
        error: function () {
            $(chkbox).prop("checked", !enabled);
        }
    });
}

// $(document).ready(function () {
$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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
    });
    makeEditable();
});