
import nose.tools as nt
from exp_decay import ExponentialDecay


def test_exp_decay():
	"""Checks that the __call__ method returns the correct value"""
	e = ExponentialDecay(0.4)
	nt.assert_almost_equals(-1.28,e(0, 3.2))
	