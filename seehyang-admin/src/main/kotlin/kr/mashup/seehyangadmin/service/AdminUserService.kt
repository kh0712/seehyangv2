package kr.mashup.seehyangadmin.service

import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.user.service.UserCommandDomain
import kr.mashup.seehyangrds.user.service.UserQueryDomain

@TransactionalService
class AdminUserService(
    private val userQueryDomain: UserQueryDomain,
    private val userCommandDoain: UserCommandDomain
) {
}