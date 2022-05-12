package errors

import com.crypto.api.v1.models.IFilter

class UnknownFilter(prod: IFilter) : Throwable("Cannot map unknown filter $prod")
