provider "aws" {
  region     = "us-east-1"
  profile = "saml"
  assume_role {
     role_arn = "arn:aws:iam::368057246517:role/LabCrossAccountAccess"
     session_name = "terraform"
   }
}

resource "aws_instance" "example" {
  subnet_id     = "subnet-514fcd7d"
  ami           = "ami-0ac019f4fcb7cb7e6"
  instance_type = "t2.micro"
}