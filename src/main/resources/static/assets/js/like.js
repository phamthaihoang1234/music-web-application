function likeBtn(btn, id){
    console.log(btn);
    $.ajax({
        method: "GET",
        url: "/like?id="+id,

    })
        .done(function( msg ) {

            btn.innerHTML = "<i class=\"far fa-heart\"></i>" + msg  ;

        });
}
