package playground


default allow =  false

default vertrag = false

vertrag {
    partnerId := input._embedded.vertragsPartner[_].partnerId
    partnerId == "M10703176"

    betrag := input._embedded.vertragsPartner[_].praemienTotal.betrag
    betrag == 381.15

    vertraege := input._embedded.vertragsPartner[_].vertraege
    hasContracts := count(vertraege)
}
