$('#edit').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget)
    let id = button.data('id')
    showEditModal(id)
})

async function showEditModal(id) {
    $('#rolesEditUser').empty()
    let user = await getUser(id)
    let form = document.forms["formEditUser"]
    form.id.value = user.id
    form.username.value = user.username
    form.age.value = user.age
    form.password.value = user.password


}