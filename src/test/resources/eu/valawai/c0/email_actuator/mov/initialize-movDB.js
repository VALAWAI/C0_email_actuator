db.createUser({
	user : 'mov',
	pwd : 'password',
	roles : [ {
		role : 'readWrite',
		db : 'movDB'
	} ]
})
