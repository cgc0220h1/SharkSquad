$(document).ready(function () {
    let $a = $("<a>", {"class": "instagram text-center"});

    $("#btn-search").click(function () {
        let keyword = $("#input-search").val();
        location.href = "/search?keyword=" + keyword;
    })

    $.ajax({
        header: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "GET",
        url: "https://api.unsplash.com/photos?page=1&client_id=IdusRIk1r39o-uOTKwKK0hmrf4Tb1Au9QI4L78NYtoQ",
        dataType: 'json',
        error: function () {
            alert("Có lỗi xảy ra")
        },
        success: function (data) {
            for (let index = 0; index < 8; index++) {
                $a.attr("href", data[index].urls.small)
                $a.css("background-image", 'url(' + data[index].urls.small + ')');
                $(".instagram-entry").append($a.clone());
            }
        }
    });
})