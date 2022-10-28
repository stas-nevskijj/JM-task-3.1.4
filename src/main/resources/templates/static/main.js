function getAllUsers() {
        fetch("http://localhost:8080/users")
        .then(res => res.json())
        .then(users => {
            let temp = ''
            console.log(users)
            users.forEach(function (user) {
                temp += `
                <tr>
                <td id="id${user.id}">${user.id}</td>
                <td id="username${user.id}">${user.username}</td>
                <td id="age${user.id}">${user.age}</td>
                <td id="password${user.id}">${user.password}</td>
                <td id="roles${user.id}">${user.roles}</td>
                <td>
                <button class = 'btn btn-info btn-md' type="button"
                data-toggle="modal" data-target="#modalEdit"
                onclick="openModal(${user.id})">
                Edit
                </button>
                </td>
                <button class = 'btn btn-danger btn-md' type="button"
                data-toggle="modal" data-target="#modalDelete"
                onclick="openModal(${user.id})">
                Delete
                </button>
                </td>
                </tr>
                `
            })
            document.getElementById('dataTable').innerHTML = temp;
        })
}
getAllUsers()


function openModal(id) {
    fetch("http://localhost:8080/users/" + id, {
        headers : {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(u => {
            console.log(u)
            document.getElementById('editId').value = u.id
            document.getElementById('editUsername').value = u.username
            document.getElementById('editAge').value = u.age
            document.getElementById('editPassword').value = u.password
            document.getElementById('editRoles').value = u.roles

            document.getElementById('deleteId').value = u.id
            document.getElementById('deleteUsername').value = u.username
            document.getElementById('deleteAge').value = u.age
            document.getElementById('deletePassword').value = u.password
            document.getElementById('deleteRoles').value = u.roles

        })
    })
}
document.getElementById('editForm')
    .addEventListener('submit', editUser)

async function editUser() {
    event.preventDefault()
    await fetch("http://localhost:8080/users" + document.getElementById('editId').value, {
        method : 'PATCH',
        headers : {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json;charset=UTF-8'
        },
        body : JSON.stringify({
            id : document.getElementById('editId').value,
            username : document.getElementById('editUsername').value,
            age : document.getElementById('editAge').value,
            password : document.getElementById('editPassword').value,
            roles : getRoles(document.getElementById('editRoles'))
        })
    }).then(response => console.log(response));
    $('#modalEdit.close').click()
    refreshTable();
}




function addNewUser() {
    event.preventDefault()
    let username = document.getElementById('createUsername').value
    let age = document.getElementById('createAge').value
    let password = document.getElementById('createPassword').value
    let roles = getRoles(document.getElementById('createRoles'))

    fetch('http://localhost:8080/users', {
        method : 'POST',
        headers : {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json;charset=UTF-8'
        },
        body : JSON.stringify({
            username : username,
            age : age,
            password : password,
            roles : roles
        })
    })
        .then(() => {
            document.getElementById('nav-table-tab').click()
            getAllUsers()
            document.getElementById('createUserForm').reset()
        })
}

async function deleteUser() {
    event.preventDefault()
    await fetch("http://localhost:8080/users" + document.getElementById('deleteId').value, {
        method : 'DELETE',
        headers : {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json;charset=UTF-8'
        }
    })
    $('#modalDelete.close').click()
    refreshTable()
}
function getRoles(selector) {
    let collection = selector.selectedOptions
    let roles = []
    for (let i = 0; i < collection.length; i++) {
        if (collection[i].value === '1') {
            roles.push({
                id : 2,
                name : 'USER'
            })
        }
    }
    return roles
}

function refreshTable() {
    let table = document.getElementById('dataTable')
    while (table.rows.length > 1) {
        table.deleteRow(1)
    }
    getAllUsers()
}