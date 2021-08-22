package com.sushmoyr.ajkalnewyork.models.stripe


import com.google.gson.annotations.SerializedName

data class PaymentResponseStripe(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("object")
    val objectX: String = "",
    @SerializedName("amount")
    val amount: Int = 0,
    @SerializedName("amount_captured")
    val amountCaptured: Int = 0,
    @SerializedName("amount_refunded")
    val amountRefunded: Int = 0,
    @SerializedName("application")
    val application: Any? = null,
    @SerializedName("application_fee")
    val applicationFee: Any? = null,
    @SerializedName("application_fee_amount")
    val applicationFeeAmount: Any? = null,
    @SerializedName("balance_transaction")
    val balanceTransaction: String = "",
    @SerializedName("billing_details")
    val billingDetails: BillingDetails = BillingDetails(),
    @SerializedName("calculated_statement_descriptor")
    val calculatedStatementDescriptor: Any? = null,
    @SerializedName("captured")
    val captured: Boolean = false,
    @SerializedName("created")
    val created: Int = 0,
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("customer")
    val customer: Any? = null,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("disputed")
    val disputed: Boolean = false,
    @SerializedName("failure_code")
    val failureCode: Any? = null,
    @SerializedName("failure_message")
    val failureMessage: Any? = null,
    @SerializedName("invoice")
    val invoice: Any? = null,
    @SerializedName("livemode")
    val livemode: Boolean = false,
    @SerializedName("on_behalf_of")
    val onBehalfOf: Any? = null,
    @SerializedName("order")
    val order: Any? = null,
    @SerializedName("outcome")
    val outcome: Any? = null,
    @SerializedName("paid")
    val paid: Boolean = false,
    @SerializedName("payment_intent")
    val paymentIntent: Any? = null,
    @SerializedName("payment_method")
    val paymentMethod: String = "",
    @SerializedName("payment_method_details")
    val paymentMethodDetails: PaymentMethodDetails = PaymentMethodDetails(),
    @SerializedName("receipt_email")
    val receiptEmail: Any? = null,
    @SerializedName("receipt_number")
    val receiptNumber: Any? = null,
    @SerializedName("receipt_url")
    val receiptUrl: String = "",
    @SerializedName("refunded")
    val refunded: Boolean = false,
    @SerializedName("refunds")
    val refunds: Refunds = Refunds(),
    @SerializedName("review")
    val review: Any? = null,
    @SerializedName("shipping")
    val shipping: Any? = null,
    @SerializedName("source_transfer")
    val sourceTransfer: Any? = null,
    @SerializedName("statement_descriptor")
    val statementDescriptor: Any? = null,
    @SerializedName("statement_descriptor_suffix")
    val statementDescriptorSuffix: Any? = null,
    @SerializedName("status")
    val status: String = "",
    @SerializedName("transfer_data")
    val transferData: Any? = null,
    @SerializedName("transfer_group")
    val transferGroup: Any? = null
)