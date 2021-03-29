function bxh(album, btn) {
    btn.classList.add("active");
    for (let i = 0; i < btn.parentElement.children.length ; i++) {
        if (i!= album){
            btn.parentElement.children[i].classList.remove("active");
        }
    }
    let bxhList = $("#bxhList");
    $.ajax({
        method: "GET",
        url: "/bxh?album="+ album,
        data: {
            name: "John",
            location: "Boston"
        }
    })
        .done(function( msg ) {
            // let bxhJson = JSON.parse(msg);
            console.log(msg);
            bxhList.empty();
            if (msg.data.content.length > 0) {
                let renderHTML = '<ul class="list-unstyled">';
                for (let i = 0; i < msg.data.content.length; i++) {
                    // console.log(msg.data[i].name);
                    renderHTML += `
                        <li class="media border-bottom py-2">
                            <div class="position-relative">
                                <a href="/music/${msg.data.content[i].id}">
                                    <img src="/files/${msg.data.content[i].image}" class="mr-3 rounded" width="85" alt="...">
                                </a>
                                <small class="position-absolute badge badge-dark text-white" style="bottom: 5px; left: 5px;">
                                <i class="fas fa-eye"></i> ${msg.data.content[i].views}
                                </small>
                            </div>
                            <div class="media-body">
                                <h5 class="mt-0 mb-1">${msg.data.content[i].name}</h5>
                                <small>${msg.data.content[i].singer}</small>
                            </div>
                            <div class="display-4 text-danger">${i+1}</div>
                        </li>`;
                }
                renderHTML += "</ul>";
                bxhList.html(renderHTML);
            }
        });
}

// $(".btn-group").button("toggle");