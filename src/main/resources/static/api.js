const url = "http://localhost:8080/api/admin/users/"

const currentUserId = document.querySelector("#user-nav-pill").getAttribute("data")
const userInfo = document.querySelector("#user-info")
let usersTable = document.querySelector("#users-table")
const addUserForm = document.addUserForm
const editUserForm = document.userEditForm

const usersTableTemplate = {
    0: "ID",
    1: "Username",
    2: "Email",
    3: "Roles",
    4: "Edit",
    5: "Delete"
}

const userFetch = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json; charset=UTF-8'
    },
    getUsers: async () => await fetch(url),
    getUser: async (id) => await fetch(url + id),
    addUser: async (user) => await fetch(url, {method: "POST", headers: userFetch.head, body: JSON.stringify(user)}),
    editUser: async (user) => await fetch(url, {method: "PUT", headers: userFetch.head, body: JSON.stringify(user)}),
    deleteUser: async (id) => await fetch(url + id, {method: "DELETE"})
}


window.onload = () => {
    getUserInfo()
    document.getElementById("edit-user-btn").addEventListener("click", editUser)
    document.getElementById("addUser").addEventListener("click", addUser)
}


async function getUserInfo() {
    await getCurrentUser().then(user => {
        const username = document.createElement("b")
        const text = document.createTextNode(" with roles: ")
        const roles = document.createTextNode(user.roles.map(role => " " + (role.name).substring(5,role.name.length)))

        if (roles.textContent.includes('ADMIN') === true) {
            adminPanel()
        } else {
            userPanel()
        }

        username.insertAdjacentText("afterbegin", user.username)

        if (!userInfo.hasChildNodes()) {
            userInfo.appendChild(username)
            userInfo.appendChild(text)
            userInfo.appendChild(roles)
        } else {
            userInfo.firstChild.replaceWith(username)
            userInfo.lastChild.replaceWith(roles)
        }
    })
}

async function getCurrentUser() {
    let response = await userFetch.getUser(currentUserId)
    return await response.json()
}

function userPanel() {
    getCurrentUser().then(user => {
        $("#users-table").remove()
        usersTable = document.createElement("table")
        usersTable.setAttribute("class", "table table-striped table-hover")
        usersTable.setAttribute("id", "users-table")
        document.querySelector("#users-card").appendChild(usersTable)

        $("#panel-title").text("User information-page")
        $("#admin-nav-tabs").addClass("collapse")
        $("#tab-name").text("About user")
        $("#admin-nav-pill").removeClass("active").prop("aria-selected", false)
        $("#user-nav-pill").addClass("active").prop("aria-selected", true)
        $("#add-user-panel").addClass("collapse")
        $("#admin-panel").removeClass("collapse")

        let tblHead = usersTable.createTHead()
        let tblBody = usersTable.createTBody()
        let tblHeadRow = tblHead.insertRow()
        let tblBodyRow = tblBody.insertRow()


        for (let i = 0; i < 4; i++) {
            let th = document.createElement("th")
            let text = document.createTextNode(usersTableTemplate[i])
            th.appendChild(text)
            tblHeadRow.appendChild(th)
        }

        tblBodyRow.insertCell().appendChild(document.createTextNode(user.id))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.username))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.email))
        tblBodyRow.insertCell().appendChild(document.createTextNode(user.roles.map(role => " " + (role.name).substring(5,role.name.length))))
    })
}

async function adminPanel() {
    $("#users-table").remove()
    usersTable = document.createElement("table")
    usersTable.setAttribute("class", "table table-striped table-hover")
    usersTable.setAttribute("id", "users-table")
    document.querySelector("#users-card").appendChild(usersTable)

    $("#panel-title").text("Admin panel")
    $("#admin-nav-tabs").removeClass("collapse")
    $("#tab-name").text("All users")
    $("#user-nav-pill").removeClass("active").prop("aria-selected", false)
    $("#admin-nav-pill").addClass("active").prop("aria-selected", true)
    $("#add-user-panel").addClass("collapse")
    $("#users-table-tab").addClass("active")
    $("#new-user-tab").removeClass("active")

    let tblHead = usersTable.createTHead()
    let tblBody = usersTable.createTBody()
    let tblHeadRow = tblHead.insertRow()

    // Filling table head
    for (let i = 0; i < 6; i++) {
        let th = document.createElement("th")
        let text = document.createTextNode(usersTableTemplate[i])
        th.appendChild(text)
        tblHeadRow.appendChild(th)
    }

    // Filling table body
    userFetch.getUsers().then(res => {
        res.json().then(users => {
            users.forEach(user => {
                let editButton = document.createElement("button")
                editButton.setAttribute("type", "button")
                editButton.setAttribute("class", "btn btn-primary")
                editButton.textContent = "Edit"
                editButton.onclick = () => {
                    getEditUserForm(user.id)
                }


                let deleteButton = document.createElement("button")
                deleteButton.setAttribute("type", "button")
                deleteButton.setAttribute("class", "btn btn-danger")
                deleteButton.textContent = "Delete"
                deleteButton.onclick = () => {
                    getDeleteUserForm(user.id)
                }

                let tblBodyRow = tblBody.insertRow()
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.id))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.username))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.email))
                tblBodyRow.insertCell().appendChild(document.createTextNode(user.roles.map(role => " " + (role.name).substring(5,role.name.length))))
                tblBodyRow.insertCell().appendChild(editButton)
                tblBodyRow.insertCell().appendChild(deleteButton)
            })
        })
    })
}

function newUserForm() {
    $("#add-user-panel").removeClass("collapse")
    $("#users-table").addClass("collapse")
    $("#tab-name").text("Add new user")
}

function addUser() {
    let user = {
        username: addUserForm.addUsername.value,
        password: addUserForm.addPassword.value,
        email: addUserForm.addEmail.value,
        roles: []
    }

    Array.from(addUserForm.addRoles.options)
        .filter(option => option.selected)
        .map(option => option.value)
        .forEach(name => {
            let role = {
                name: ""
            }
            if (name === "ROLE_ADMIN") {
                role.name = "ROLE_ADMIN"
            }
            if (name === "ROLE_USER") {
                role.name = "ROLE_USER"
            }
            user.roles.push(role)
        })

    console.log(user)

    userFetch.addUser(user).then((response) => {
        document.getElementById('addUsername').value = ""
        document.getElementById('addPassword').value = ""
        document.getElementById('addEmail').value = ""
        document.getElementById('addRoles').value = ""
        if (response.ok) {
            adminPanel();
        } else {
            alert("Error. Try again")
            newUserForm();
        }
    })

}

function getEditUserForm(id) {
    userFetch.getUser(id)
        .then((res) => {
            res.json().then((user) => {
                console.log(user)
                $("#editId").val(user.id)
                $("#editUsername").val(user.username)
                $("#editPassword").val("")
                $("#editEmail").val(user.email)
                $("#editRoles").val(user.roles)
            })
            $("#editModal").modal()
        })
}




function editUser() {
    const user = {
        id: document.userEditForm.editId.value,
        username: document.userEditForm.editUsername.value,
        password: document.userEditForm.editPassword.value,
        email: document.userEditForm.editEmail.value,
        roles: []
    }

    Array.from(editUserForm.editRoles.options)
        .filter(option => option.selected)
        .map(option => option.value)
        .forEach(name => {
            let role = {
                name: ""
            }
            if (name === "ROLE_ADMIN") {
                role.name = "ROLE_ADMIN"
            }
            if (name === "ROLE_USER") {
                role.name = "ROLE_USER"
            }
            user.roles.push(role)
        })

    console.log(user)

    userFetch.editUser(user).then(res => {
        if (res.ok) {
            console.log("User with username \"" + user.username + "\" updated")
            $("#editModal").modal("hide")
            adminPanel()
            if (user.id === currentUserId) {
                getUserInfo()
            }
        } else {
            getEditUserForm(user.id)
        }
    }).catch(e => console.log(e))
}

function getDeleteUserForm(id) {
    userFetch.getUser(id)
        .then((res) => {
            res.json().then((user) => {
                console.log(user)
                $("#deleteId").val(user.id)
                $("#deleteUsername").val(user.username)
                $("#deleteEmail").val(user.email)
                $("#deleteRoles").val(user.roles)
            })
            $("#deleteModal").modal()
        })
}

function deleteUser() {
    userFetch.deleteUser($("#deleteId").val())
        .then(res => {
            if (res.ok) {
                adminPanel();
                $("#deleteModal").modal("hide")
            } else {
                alert("Error. Try again")
                getDeleteUserForm();
            }
        })
}