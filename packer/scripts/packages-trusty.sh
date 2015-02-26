set -e

apt-get -y install sudo rsync

echo 'APT::Install-Recommends "0";' > /etc/apt/apt.conf.d/50no-install-recommends
echo 'APT::Install-Suggests "0";' > /etc/apt/apt.conf.d/50no-install-suggests

apt-get -y update
apt-get -y upgrade

apt-get -y install autoconf automake bison cpp flex gcc gdb git libc6-dev libtool m4 make pkg-config strace valgrind
apt-get -y install autotools-dev default-jdk iptables-dev javahelper libatasmart-dev libcurl4-gnutls-dev libdbi0-dev libesmtp-dev libganglia1-dev libgcrypt11-dev libglib2.0-dev libhiredis-dev libi2c-dev libldap2-dev libltdl-dev liblvm2-dev libmemcached-dev libmnl-dev libmodbus-dev libmysqlclient-dev libnotify-dev libopenipmi-dev liboping-dev libow-dev libpcap-dev libperl-dev libpq-dev libprotobuf-c0-dev librabbitmq-dev librdkafka-dev librrd-dev libsensors4-dev libstatgrab-dev libsigrok-dev libsnmp-dev libtokyocabinet-dev libtokyotyrant-dev libudev-dev libupsclient-dev libvarnish-dev libvirt-dev libxml2-dev libyajl-dev linux-libc-dev perl protobuf-c-compiler python-dev

apt-get -y clean
